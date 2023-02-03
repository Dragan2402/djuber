package com.djuber.djuberbackend.Infastructure.Repositories.Route;

import com.djuber.djuberbackend.Domain.Client.Client;
import com.djuber.djuberbackend.Domain.Route.FavouriteRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface IFavouriteRouteRepository  extends JpaRepository<FavouriteRoute, Long> {

    @Query("select distinct fv From FavouriteRoute fv join fetch fv.stopNames sn where fv.client.id=?1")
    List<FavouriteRoute> findByClientId(Long clientId);

}
