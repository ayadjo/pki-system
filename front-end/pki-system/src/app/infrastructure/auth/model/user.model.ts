import { Role } from "./role.model";

export interface User {
  id: number;
  mail: string;
  password: string;
  roles: Role[];
  commonName: string;
  surname: string;
  givenName: string;
  organization: string;
  organizationUnit: string;
  country: string;
}

