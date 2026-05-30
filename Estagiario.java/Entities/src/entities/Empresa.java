package entities;

public class Empresa extends Usuario {
    private String cnpj;
    private String inscricaoEstadual;
    private String site;
    private String telefone;
    private String contato;

    // metodo construtor
    public Empresa() {
            super();
            cnpj=new String();
            inscricaoEstadual=new String();
            site=new String();
            contato=new String();

    }

    public Empresa(int id, String nome, String email, String senha, String cnpj, String inscricaoEstadual,
            String site, String telefone, String contato) {
        super(id, nome, email, senha);
        this.cnpj = cnpj;
        this.inscricaoEstadual = inscricaoEstadual;
        this.site = site;
        this.telefone = telefone;
        this.contato = contato;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }
    

}
