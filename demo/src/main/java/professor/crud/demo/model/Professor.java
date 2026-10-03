package professor.crud.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@Entity 
@Table(name="professor")
public class Professor {
    @Id 
    private Long id ; 
    @Column(nullable = false, length = 100)
    private String nome;
    @Column(nullable = false, unique = true, length = 150)
    private String email;
    @Column(length = 100)
    private String area;
    @Column(length = 20)
    private String telefone;

    public Professor(){}
}
