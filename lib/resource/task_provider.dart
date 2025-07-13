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
    return Task(id: task['id'], type: TaskType.fromString(task['type']), state: TaskState.fromString(task['state']), name: task['name'], value: task['value']);
  }

  Future<void> createTask(Task task) async {
    await db.collection('tasks').insertOne(task.toJson());
  }
}