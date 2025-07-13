import 'package:shelf_router/shelf_router.dart';
import 'package:shelf/shelf.dart';
import 'package:ktc/resource/task_provider.dart';
import 'package:ktc/controller/task_controller.dart';
import 'package:mongo_dart/mongo_dart.dart';
import 'package:logging/logging.dart';

class KtcRouter {
  final Db db;
  final Logger log;
  late Router router;
  KtcRouter(this.db, this.log) {
    final getTaskController = GetTaskController(TaskProvider(db), log);
    final postTaskController = PostTaskController(TaskProvider(db), log);
    router = Router()
      ..get('/task', (Request request) async => await getTaskController.handle(request))
      ..post('/task', (Request request) async => await postTaskController.handle(request))
      ..get('/hello', (Request request) => Response.ok('Hello, World!'));
  }
}