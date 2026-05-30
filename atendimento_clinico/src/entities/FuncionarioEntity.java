package entities;

public class FuncionarioEntity extends PessoaEntity {
    private String setor;

    public FuncionarioEntity() {
        super(); // chama o construtor da classe pai
        setor = new String();
    }
    // Construtor, getters e setters- botao direito do mouse source action constructors e com parametros e outro sem parametros, depois source action getters and setters e seleciona o atributo que quer criar os getters e setters
    public FuncionarioEntity(String cpf, String nome, String senha, StatusEntity status, String setor) {
        super(cpf, nome, senha, status);
        this.setor = setor;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }
      @Override
    public String mostrarStatus(){
        return "Funcionário: " + getNome() +
        "- Status" + getStatus().getDescricao();
    }

}
