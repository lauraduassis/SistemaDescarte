package entities;

public class FuncionarioEmpresaEntity extends PessoaEntity {
    private EmpresaColetoraEntity empresa;

    public FuncionarioEmpresaEntity() {
        super();
        empresa = new EmpresaColetoraEntity();
    }

    public FuncionarioEmpresaEntity(EmpresaColetoraEntity empresa) {
        super();
        this.empresa = empresa;
    }

    public FuncionarioEmpresaEntity(int id, String nome, String cpf, String email, String fone,
            EmpresaColetoraEntity empresa) {
        super(id, nome, cpf, email, fone);
        this.empresa = empresa;
    }

    public EmpresaColetoraEntity getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(EmpresaColetoraEntity empresa) {
        this.empresa = empresa;
    }

    @Override
    public String mostrarStatus() {
        return "Funcionário Empresa: " + getNome();
    }
}
