package switchtwentytwenty.project.dto.toservicedto;

import switchtwentytwenty.project.dto.indto.FamilyAndAdminInDTO;

public abstract class FamilyAndAdminDTOMapper {

    /**
     * Private constructor.
     */
    private FamilyAndAdminDTOMapper() {}

    /**
     * Map to FamilyAndAdmin dto.
     *
     * @param info - family and admin dto.
     * @return The person dto.
     */
    public static FamilyAndAdminDTO mapToDTO(FamilyAndAdminInDTO info) {
        FamilyAndAdminDTO newDTO = new FamilyAndAdminDTO();
        newDTO.setPersonName(info.getPersonName());
        newDTO.setBirthDate(info.getBirthDate());
        newDTO.setVat(info.getVat());
        newDTO.setHouseNumber(info.getHouseNumber());
        newDTO.setStreet(info.getStreet());
        newDTO.setCity(info.getCity());
        newDTO.setCountry(info.getCountry());
        newDTO.setZipCode(info.getZipCode());
        newDTO.setPhoneNumbers(info.getPhoneNumbers());
        newDTO.setEmail(info.getEmail());
        newDTO.setFamilyName(info.getFamilyName());
        newDTO.setUsername(info.getUsername());
        newDTO.setPassword(info.getPassword());
        return newDTO;
    }
}
