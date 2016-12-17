import { TlmfrontPage } from './app.po';

describe('tlmfront App', function() {
  let page: TlmfrontPage;

  beforeEach(() => {
    page = new TlmfrontPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
