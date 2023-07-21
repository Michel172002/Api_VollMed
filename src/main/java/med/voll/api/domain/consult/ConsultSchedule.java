package med.voll.api.domain.consult;

import med.voll.api.domain.ValidationException;
import med.voll.api.domain.consult.validations.ValidatorConsultSchedule;
import med.voll.api.domain.doctor.Doctor;
import med.voll.api.domain.doctor.DoctorRepository;
import med.voll.api.domain.patient.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultSchedule {

    @Autowired
    private ConsultRepository consultRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private List<ValidatorConsultSchedule> validators;

    public void schedule(ConsultScheduleData data){
        if(!patientRepository.existsById(data.idPatient())){
            throw new ValidationException("Não existe paciente com o id Informado!");
        }

        if(!doctorRepository.existsById(data.idDoctor()) && data.idDoctor() != null){
            throw new ValidationException("Não existe paciente com o id Informado!");
        }

        validators.forEach(v -> v.validate(data));

        var patient = patientRepository.getReferenceById(data.idPatient());
        var doctor = choiceDoctor(data);

        var consult = new Consult(null, doctor, patient, data.date());

        consultRepository.save(consult);
    }

    private Doctor choiceDoctor(ConsultScheduleData data) {
        if(data.idDoctor() != null){
            return doctorRepository.getReferenceById(data.idDoctor());
        }

        if(data.specialty() == null){
            throw new ValidationException("Expecialidade é obrigatoria quando medico não é escolhido!");
        }

        return doctorRepository.choiceRandomDoctorFree(data.specialty(), data.date());
    }
}