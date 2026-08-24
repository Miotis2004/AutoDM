export interface SystemSettingsDto {
  dataDir: string;
  dbName: string;
}

export interface UserSettings {
  theme: 'light' | 'dark';
  fontSize: 'small' | 'medium' | 'large';
}
