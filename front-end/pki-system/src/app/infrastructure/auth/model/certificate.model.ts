export interface Certificate {
    serialNumber: string;
    revoked: boolean;
    certificateType: CertificateType;
    issuerMail: string;
    subjectMail: string;
    startDate: Date;
    endDate: Date;
}

export enum CertificateType {
    ROOT = 'ROOT',
    CA = 'CA',
    EE = 'EE'
  }