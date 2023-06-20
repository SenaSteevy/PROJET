import { TaskDescription } from "./TaskDescription";

export interface Job {
    numOrder: number;
    taskDescription: TaskDescription;
    type: string;
    dueDate: Date; // Change to appropriate date type if needed
    taskList: Task[];
    startDateTime: Date; // Change to appropriate date type if needed
    leadTime: Date; // Change to appropriate duration type if needed
    priority: number;
    status: string;
  }