package valuta.kzt;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by BahaWood on 1/27/19.
 */
@Repository
public interface ValutaRepository extends CrudRepository<ValutaModel, Float>{

    @Query(value = " select * from valuta_model where from_to=(:fromTo) order by date desc limit 1", nativeQuery = true)
    public ValutaModel findByFrom(@Param("fromTo") String from_to);
}
