package com.mcgrady.xproject.feature.pokemon.data.database.entity.mapper

import com.mcgrady.xproject.core.mapper.EntityMapper
import com.mcgrady.xproject.feature.pokemon.data.database.entity.PokemonInfoEntity
import com.mcgrady.xproject.feature.pokemon.data.model.PokemonInfo

object PokemonInfoEntityMapper : EntityMapper<PokemonInfo, PokemonInfoEntity> {
    override fun asEntity(domain: PokemonInfo): PokemonInfoEntity {
        return PokemonInfoEntity(
            id = domain.id,
            name = domain.name,
            height = domain.height,
            weight = domain.weight,
            experience = domain.experience,
            types = domain.types,
            hp = domain.hp,
            attack = domain.attack,
            defense = domain.defense,
            speed = domain.speed,
            exp = domain.exp
        )
    }

    override fun asDomain(entity: PokemonInfoEntity): PokemonInfo {
        return PokemonInfo(
            id = entity.id,
            name = entity.name,
            height = entity.height,
            weight = entity.weight,
            experience = entity.experience,
            types = entity.types,
            hp = entity.hp,
            attack = entity.attack,
            defense = entity.defense,
            speed = entity.speed,
            exp = entity.exp
        )
    }
}

fun PokemonInfo.asEntity(): PokemonInfoEntity {
    return PokemonInfoEntityMapper.asEntity(this)
}

fun PokemonInfoEntity.asDomain(): PokemonInfo {
    return PokemonInfoEntityMapper.asDomain(this)
}
