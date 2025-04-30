declare const VERSION: string;
declare const __SERVER_API_URL__: string;
declare const I18N_HASH: string;

declare module '*.json' {
  const value: any;
  export default value;
}
