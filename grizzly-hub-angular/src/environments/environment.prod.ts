export const environment = {
  production: true,
  baseUrl: '',
  url: location.origin + '/',
  GITHUB_AUTHORIZE_URL: 'https://github.com/login/oauth/authorize',
  GITHUB_REDIRECT_URI: location.origin + '/api/auth/github/login',
  GITHUB_CLIENT_ID: 'ede9ba8bee8f9c247fd2'
};
