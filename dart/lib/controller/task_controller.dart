import 'package:ktc/resource/task_provider.dart';
import 'package:shelf/shelf.dart';
import 'dart:convert';
import 'package:logging/logging.dart';

abstract class TaskController {
  Future<Response> handle(Request request);
}

class GetTaskController extends TaskController {
  final TaskProvider taskProvider;
  final Logger log;
  GetTaskController(this.taskProvider, this.log);

  @override
  Future<Response> handle(Request request) async {
    final id = request.url.queryParameters['id'];
    log.fine('GetTaskController: $id');
    if (id != null && id.isNotEmpty) {
      final task = await taskProvider.getTask(id);
      if (task.isEmpty()) {
        log.fine('Task not found');
        return Response.notFound('Task not found');
      }
      log.fine('Task found: ${jsonEncode(task)}');
      return Response.ok(jsonEncode(task));
    } else {
      log.fine('No id provided');
      return Response.ok('Hello, World!');
    }
  }
}

class PostTaskController extends TaskController {
  final TaskProvider taskProvider;
  final Logger log;
  PostTaskController(this.taskProvider, this.log);

  @override
  Future<Response> handle(Request request) async {
    final bodyStr = await request.readAsString();
    Map<String, dynamic> body;
    if (request.encoding == Encoding.getByName('application/json')) {
        body = jsonDecode(bodyStr);
    } else if (request.encoding == Encoding.getByName('application/x-www-form-urlencoded')) {
        body = Uri.splitQueryString(bodyStr);
    } else {
        return Response.badRequest(body: 'Invalid content type');
    }
    await taskProvider.createTask(body);
    return Response.ok(jsonEncode(body));
  }
}
