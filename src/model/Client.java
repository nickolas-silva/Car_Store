package model;

public class Client extends User{
    
    public Client(String name, String cpf, String password) {
        super.setCpf(cpf);
        super.setName(name);;
        super.setPassword(password);;

    }
    
}
