package entities;

public class FuncionarioAlmoxarifadoEntity extends PessoaEntity {

    private CargoEntity cargo;

    public FuncionarioAlmoxarifadoEntity() {
        super();
        cargo = new CargoEntity();
    }

    public FuncionarioAlmoxarifadoEntity(int id, String nome, String cpf, String email, String fone,
            CargoEntity cargo) {
        super(id, nome, cpf, email, fone);
        this.cargo = cargo;
    }

    public CargoEntity getCargo() {
        return cargo;
    }

    public void setCargo(CargoEntity cargo) {
        this.cargo = cargo;
    }

    @Override
    public String mostrarStatus() {
        return "Funcionário Almoxarifado: " + getNome();
    }
}
