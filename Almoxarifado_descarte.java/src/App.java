import java.time.LocalDate;

import entities.CargoEntity;
import entities.EmpresaColetoraEntity;
import entities.EntradaEntity;
import entities.EntradaProduto;
import entities.FuncionarioAlmoxarifadoEntity;
import entities.FuncionarioEmpresaEntity;
import entities.NotaDescarteEntity;
import entities.NotaDescarteProduto;
import entities.ProdutoEntity;
import entities.RetiradaEntity;
import entities.RetiradaProduto;

public class App {

    public static void main(String[] args) {

        // Cargo
        CargoEntity cargo = new CargoEntity(
                1,
                "Auxiliar de Almoxarifado"
        );

        // Funcionário do Almoxarifado
        FuncionarioAlmoxarifadoEntity funcAlmox =
                new FuncionarioAlmoxarifadoEntity();

        funcAlmox.setNome("João");
        funcAlmox.setCpf("123456789");
        funcAlmox.setCargo(cargo);

        // Empresa Coletora
        EmpresaColetoraEntity empresa =
                new EmpresaColetoraEntity(
                        1,
                        "Eco Reciclagem",
                        "12345678000100",
                        "contato@eco.com",
                        "17999999999"
                );

        // Funcionário da Empresa
        FuncionarioEmpresaEntity funcEmpresa =
                new FuncionarioEmpresaEntity();

        funcEmpresa.setNome("Carlos");
        funcEmpresa.setCpf("987654321");
        funcEmpresa.setEmpresa(empresa);

        // Produto
        ProdutoEntity produto =
                new ProdutoEntity(
                        1,
                        "Monitor Dell",
                        "PAT001",
                        LocalDate.of(2026, 6, 1)
                );

        // Entrada
        EntradaEntity entrada = new EntradaEntity();

        entrada.setCodigo(1);
        entrada.setStatus("Recebido");
        entrada.setObs("Equipamento em bom estado");

        // EntradaProduto
        EntradaProduto entradaProduto =
                new EntradaProduto();

        entradaProduto.setProduto(produto);
        entradaProduto.setQtd(10);

        // Retirada
        RetiradaEntity retirada = new RetiradaEntity();

        retirada.setCodigo(1);
        retirada.setStatus("Concluida");

        // RetiradaProduto
        RetiradaProduto retiradaProduto =
                new RetiradaProduto();

        retiradaProduto.setProduto(produto);
        retiradaProduto.setQtd(2);

        // Nota de Descarte
        NotaDescarteEntity nota =
                new NotaDescarteEntity();

        nota.setNrPatrimonio(1);
        nota.setObservacao("Produto obsoleto");

        // NotaDescarteProduto
        NotaDescarteProduto notaProduto =
                new NotaDescarteProduto();

        notaProduto.setProduto(produto);
        notaProduto.setQtd(3);

        // Exibição dos dados

        System.out.println("=== PRODUTO ===");
        System.out.println(produto.getDescricao());

        System.out.println("\n=== FUNCIONÁRIO ALMOXARIFADO ===");
        System.out.println(funcAlmox.mostrarStatus());

        System.out.println("\n=== FUNCIONÁRIO EMPRESA ===");
        System.out.println(funcEmpresa.mostrarStatus());

        System.out.println("\nQuantidade Entrada: "
                + entradaProduto.getQtd());

        System.out.println("Quantidade Retirada: "
                + retiradaProduto.getQtd());

        System.out.println("Quantidade Descarte: "
                + notaProduto.getQtd());

    }
}
