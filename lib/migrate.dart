import 'package:mongo_dart/mongo_dart.dart';
import 'package:logging/logging.dart';

final migrationCollectionName = 'migration';

Future<void> init(Db db) async {
  final log = Logger('ktc');
  final collectionNames = await db.getCollectionNames();
  if (collectionNames.contains(migrationCollectionName)) {
    log.fine('Migration collection already exists');
    return;
  }
  await db.createCollection(migrationCollectionName);
  log.fine('Migration collection created');
  await db.collection(migrationCollectionName).insertOne({
    'name': 'current',
    'version': 0,
  });
  log.fine('Migration collection initialized');
}

abstract class Migration {
  final int version;
  Migration(this.version);
  Future<bool> up(Db db);
  Future<bool> down(Db db);
}

class Migration1 extends Migration {
  Migration1() : super(1);

  @override
  Future<bool> up(Db db) async {
    await db.collection('tasks').createIndex(key: 'id', unique: true);
    return true;
  }

  @override
  Future<bool> down(Db db) async {
    DbCollection collection = db.collection('tasks');
    List<Map<String, dynamic>> indexes = await collection.getIndexes();
    for (Map<String, dynamic> index in indexes) {
      final keyMap = index['key'] as Map<String, dynamic>;
      if (keyMap.length == 1 && keyMap.containsKey('id')) {
        collection.dropIndexes(index);
      }
    }
    return true;
  }
}


List<Migration> migrations = [
  Migration1(),
];

Future<void> migrate({required Db db, int version = 0}) async {
  final log = Logger('ktc');
  await init(db);
  final migrationCollection = db.collection(migrationCollectionName);
  final migration = await migrationCollection.findOne(where.eq('name', 'current'));
  int current = 0;
  if (migration != null) {
    current = migration['version'];
  }
  log.fine('Current migration version: $current');
  if (version != 0 && current > version) {
    for (int i = current; i >= version; i--) {
      if (i == 0) {
        continue;
      }
      final migration = migrations[i - 1];
      await migration.down(db);
    }
    current = version;
    log.fine('Down migration version: $current');
  } else {
    for (current++; current <= migrations.length; current++) {
      if (version != 0 && current > version) {
          break;
      }
      final migration = migrations[current - 1];
      await migration.up(db);
      log.fine('Up migration version: $current');
    }
    current--;
  }
  await migrationCollection.updateOne(where.eq('name', 'current'), {
    '\$set': {
      'version': current,
    },
  });
  log.fine('Migration completed');
}