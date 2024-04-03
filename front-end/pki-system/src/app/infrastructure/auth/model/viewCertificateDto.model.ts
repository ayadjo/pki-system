import { CertificateType } from "./certificate.model";
import { UserDataDto } from "./userDataDto.model";

//prikaz jednog sertifikata
export interface ViewCertificateDto{
    subjectData: UserDataDto,
    issuerData: UserDataDto,
    serialNumber: String,
    startDate: Date,
    endDate: Date,
    certificateType: CertificateType,
    revoked: boolean
    keyUsages: String[];
}
