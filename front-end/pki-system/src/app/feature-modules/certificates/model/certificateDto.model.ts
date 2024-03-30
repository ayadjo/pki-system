
export enum CertificateType {
    ROOT = 'ROOT',
    CA = 'CA',
    EE = 'EE'
  }
export interface CertificateDto {
    issuerMail: string;
    subjectMail: string;
    issuerCertificateSerialNumber: string;
    issuerCertificateType: CertificateType;
    subjectCertificateType: CertificateType;
    startDate: Date;
    endDate: Date;
}

