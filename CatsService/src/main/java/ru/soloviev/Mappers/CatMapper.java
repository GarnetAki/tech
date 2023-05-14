package ru.soloviev.Mappers;

import ru.soloviev.Dao.CatDao;
import ru.soloviev.Dto.CatDto;
import ru.soloviev.Dto.CatIdDto;
import ru.soloviev.Entities.Cat;
public class CatMapper {

    private static CatDao catDao;

    public CatMapper(CatDao catDao) {
        CatMapper.catDao = catDao;
    }

    public static Cat mapToEntity(CatIdDto dto) {
        return catDao.findById(dto.getId()).orElseThrow();
    }

    public static Cat mapToEntityUp(CatDto dto) {
        return catDao.findById(dto.getId()).orElseThrow();
    }

    public static Cat mapToEntity(CatDto dto) {
        return new Cat(dto.getId(), dto.getName(), dto.getBreed(), dto.getColor(),
                dto.getDateOfBirth(), dto.getOwnerId());
    }

    public static CatIdDto mapToIdDto(Cat cat) {
        CatIdDto catIdDto = new CatIdDto();
        catIdDto.setId(cat.getId());
        return catIdDto;
    }

    public static CatDto mapToDto(Cat cat) {
        CatDto catDto = new CatDto();
        catDto.setId(cat.getId());
        catDto.setName(cat.getCatName());
        catDto.setBreed(cat.getBreed());
        catDto.setColor(cat.getColor());
        catDto.setDateOfBirth(cat.getDateOfBirth());
        catDto.setOwnerId(cat.getOwner());
        catDto.setFriends(cat.getFriends().stream().map(CatMapper::mapToIdDto).toList());
        return catDto;
    }

    public static String mapToString(CatDto cat) {
        return cat.getId() + " | " +
                cat.getName().getName() + " | " +
                cat.getColor() + " | " +
                cat.getDateOfBirth() + " | " +
                cat.getBreed().getBreed() + " | " +
                cat.getOwnerId();
    }
}
