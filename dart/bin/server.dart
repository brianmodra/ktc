import 'dart:io';

import 'package:shelf/shelf.dart';
import 'package:shelf/shelf_io.dart' as io;
import 'package:mongo_dart/mongo_dart.dart';
import 'package:ktc/ktc_router.dart';
import 'package:ktc/migrate.dart';
import 'package:logging/logging.dart' as logging;

final host = Platform.environment['HOST'] ?? '0.0.0.0';
final port = int.parse(Platform.environment['PORT'] ?? '8080');
final dbhost = Platform.environment['MONGO_HOST'] ?? '127.0.0.1';
final dbport = Platform.environment['MONGO_PORT'] ?? '27017';

void main(List<String> args) async {
  logging.hierarchicalLoggingEnabled = true;
  final log = logging.Logger('ktc');
  log.level = logging.Level.ALL;
  final httpLog = logging.Logger('http');
  httpLog.level = logging.Level.ALL;

  log.onRecord.listen((record) {
    print('${record.level.name}: ${record.time.toIso8601String()}: ${record.message}');
  });
  httpLog.onRecord.listen((record) {
    print('${record.level.name}: ${record.message}');
  });
  log.fine('Starting server');

  final db = Db('mongodb://$dbhost:$dbport/ktc');
  await db.open();
  await migrate(db: db);

  final router = KtcRouter(db, log);

  void logger(String message, bool isError) {
    if (isError) {
      httpLog.warning(message);
    } else {
      httpLog.info(message);
    }
  }

  // Configure a pipeline that logs requests.
  final handler = Pipeline()
      .addMiddleware(logRequests(logger: logger))
      .addHandler(router.router.call);

  final server = await io.serve(handler.call, host, port);
  log.info('Serving at http://${server.address.host}:${server.port}');
}
