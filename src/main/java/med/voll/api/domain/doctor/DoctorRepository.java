package med.voll.api.domain.doctor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Page<Doctor> findAllByActiveTrue(Pageable pageable);

    @Query("""
            select d from Doctor d
            where 
            d.active = true
            and
            d.specialty = :specialty
            and
            d.id not in(
                select c.doctor.id from Consult c
                where
                c.date = :date
                and
                c.reason is null
            )
            order by random()
            limit 1          
            """)
    Doctor choiceRandomDoctorFree(Specialty specialty, LocalDateTime date);

    @Query("""
            select d.active
            from Doctor d
            where
            d.id = :id
            """)
    Boolean findActiveById(Long id);
}
