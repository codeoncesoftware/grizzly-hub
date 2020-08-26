// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  baseUrl: '',
  url: location.origin + '/',
  GITHUB_AUTHORIZE_URL: 'https://github.com/login/oauth/authorize',
  GITHUB_REDIRECT_URI: location.origin + '/api/auth/github/login',
  GITHUB_CLIENT_ID: 'ede9ba8bee8f9c247fd2'
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
