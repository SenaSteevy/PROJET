import { Job } from "./Job";
import { Phase } from "./Phase";

export interface Task {
    id: number;
    job: Job;
    phase: Phase;
    type: string;
    status: string;
    startTime: Date; // Change to appropriate date type if needed
  }