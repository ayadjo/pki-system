//info o subjectu i issueru u okviru jednog sertifikata
export interface UserDataDto {
    userId: String,
    mail: String,
    country: String,
    organizationUnit: String,
    organization: String,
    givenName: String,
    surname: String,
    commonName: String
}