package Model;

/**
 *
 * @author augustosimionato
 */
public class Employee {
    
    private int idEmployee;
    private String nomeCompleto;
    private String cpf;
    private String endereco;
    private String cep;
    private String telefone;
    private String contratacao;
    private String demissao;
    
    public Employee(int ie, String nc, String cf, String end, String ce, String tel, String con, String de) {
        this.idEmployee = ie;
        this.nomeCompleto = nc;
        this.cpf = cf;
        this.endereco = end;
        this.cep = ce;
        this.telefone = tel;
        this.contratacao = con;
        this.demissao = de;
    }

    @Override
    public String toString() {
        if (this != null) {
            return (nomeCompleto + "  " + cpf + "  " + endereco + "  " + cep + "  " + telefone + "  " + contratacao + "  " + demissao);
        } else {
            return (null);
        }
    }

    /**
     * @return the nomeCompleto
     */
    public String getNomeCompleto() {
        return nomeCompleto;
    }

    /**
     * @param nomeCompleto the nomeCompleto to set
     */
    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    /**
     * @return the cpf
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * @param cpf the cpf to set
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * @return the endereco
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     * @param endereco the endereco to set
     */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    /**
     * @return the cep
     */
    public String getCep() {
        return cep;
    }

    /**
     * @param cep the cep to set
     */
    public void setCep(String cep) {
        this.cep = cep;
    }

    /**
     * @return the telefone
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * @param telefone the telefone to set
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * @return the contratacao
     */
    public String getContratacao() {
        return contratacao;
    }

    /**
     * @param contratacao the contratacao to set
     */
    public void setContratacao(String contratacao) {
        this.contratacao = contratacao;
    }

    /**
     * @return the demissao
     */
    public String getDemissao() {
        return demissao;
    }

    /**
     * @param demissao the demissao to set
     */
    public void setDemissao(String demissao) {
        this.demissao = demissao;
    }

    /**
     * @return the idEmployee
     */
    public int getIdEmployee() {
        return idEmployee;
    }

    /**
     * @param idEmployee the idEmployee to set
     */
    public void setIdEmployee(int idEmployee) {
        this.idEmployee = idEmployee;
    }
    
}
