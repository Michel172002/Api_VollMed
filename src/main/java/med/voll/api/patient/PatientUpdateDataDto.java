package med.voll.api.patient;

import jakarta.validation.constraints.NotNull;
import med.voll.api.address.AddressDto;

public record PatientUpdateDataDto(
        @NotNull
        Long id,
        String name,
        String phone,
        AddressDto address
) {
}