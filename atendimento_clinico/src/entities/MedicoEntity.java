package entities;

public class MedicoEntity extends PessoaEntity {
    private String crm;

    public MedicoEntity() {
        super(); // Chama o construtor da classe pai (PessoaEntity)
        crm = new String();
    }

    // Construtor, getters e setters
    public MedicoEntity(String cpf, String nome, String senha, StatusEntity status, String crm) {
        super(cpf, nome, senha, status);
        this.crm = crm;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }
      @Override
    public String mostrarStatus(){
        return "Médico: " + getNome() + "-CRM: " + getCrm() +
        "- Status" + getStatus().getDescricao();
    }


}
