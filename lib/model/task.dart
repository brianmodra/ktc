enum TaskState {
  todo,
  inProgress,
  done,
  cancelled;

  static TaskState fromString(String str) {
    return TaskState.values.firstWhere((t) => t.name == str);
  }
}

enum TaskType {
  ThirdPersonDeepPOV;

  static TaskType fromString(String str) {
    return TaskType.values.firstWhere((t) => t.name == str);
  }
}

class Task {
  final String id;
  final TaskType type;
  final TaskState state;
  final String name;
  final String value;
  Task({required this.id, required this.type, required this.state, required this.name, required this.value});
  Task.empty() : this(id: '', type: TaskType.ThirdPersonDeepPOV, state: TaskState.todo, name: '', value: '');
  bool isEmpty() => id == '';
  Map<String, dynamic> toJson() => {
    'id': id,
    'type': type.name,
    'state': state.name,
    'name': name,
    'value': value,
  };
}