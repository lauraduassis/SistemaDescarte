package entities;
import java.time.LocalDateTime;

public class AdiminEntity extends PessoaEntity{
    private LocalDateTime dataCadastro;

    public AdiminEntity() {
        super();//faz isso automaticamente, chama o construtor da classe pai
        dataCadastro = LocalDateTime.now();
    }
    //Construtor, getters e setters
    public AdiminEntity(String cpf, String nome, String senha, StatusEntity status, LocalDateTime dataCadastro) {
        super(cpf, nome, senha, status);
        this.dataCadastro = dataCadastro;
    }
    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
    @Override
    public String mostrarStatus(){
        return "Administrador: " + getNome() +
        "- Status" + getStatus().getDescricao();
    }
    

}
