import 'package:ktc/model/task.dart';
import 'package:mongo_dart/mongo_dart.dart';

class TaskProvider {
  final Db db;
  TaskProvider(this.db);

  Future<Task> getTask(String id) async {
    final task = await db.collection('tasks').findOne(where.eq('id', id));
    if (task == null) {
      return Task.empty();
    }
    return Task(id: task['id'], title: task['title'], description: task['description']);
  }

  Future<void> createTask(Map<String, dynamic> body) async {
    await db.collection('tasks').insertOne(body);
  }
}