package fr.codeonce.grizzlyhub.microservices.domain;


import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

        SubscriptionDto mapToDto(Subscription entity);

        Subscription mapToDomain(SubscriptionDto dto);


}
