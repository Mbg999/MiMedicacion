import { Taken } from './taken';

export interface Medication {
    id?: number;
    user_id?: number;
    name?: string;
    description?: string;
    hours_interval?: number;
    finished?: boolean;
    created_at?: string;
    updated_at?: string;
    last_taken?: Taken;
    takens?: Taken[];
}
