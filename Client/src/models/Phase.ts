export interface Phase {
    id: number;
    name: string;
    capacity: number;
    duration: string; // Change to appropriate duration type if needed
    taskList: Task[];
  }