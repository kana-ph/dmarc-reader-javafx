package ph.kana.dmarcreader.model;

public class AuthResult {

    private final AuthType type;
    private String domain;
    private String selector;
    private String result;

    public AuthResult(AuthType type) {
        this.type = type;
    }

    public AuthType getType() {
        return type;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
