package com.canehealth.ckblib.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.canehealth.ckblib.graph.model.DiseaseDisorderMention;
import com.canehealth.ckblib.graph.model.SignSymptomMention;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SignSymptomService {

    @Autowired
    SignSymptomRepository signSymptomMentionRepository;

    @Autowired
    Driver driver;

    public Mono<SignSymptomMention> getSymptomByCui(String cui) {
        return signSymptomMentionRepository.findOneByCui(cui);
    }

    @Transactional
    public Mono<SignSymptomMention> saveSymptom(SignSymptomMention signSymptomMention) {
        return signSymptomMentionRepository.save(signSymptomMention);
    }

    public Flux<SignSymptomMention> getSymptomByName(String name) {
        return signSymptomMentionRepository.findAllByNameLikeIgnoreCase(name);
    }

    // public Flux<DiseaseDisorderMention> getDiseases(String cui) {
    //     return signSymptomMentionRepository.findAllDiseasesWithSymptomsByCui(cui);
    // }

    public Mono<SignSymptomMention> addRelation(String dcui, String scui) {
            return signSymptomMentionRepository.mergeDiseaseWithSymptom(dcui, scui);
    }

    /**
     * This is an example of when you might want to use the pure driver in case you
     * have no need for mapping at all, neither in the form of the way the
     * {@link org.springframework.data.neo4j.core.Neo4jClient} allows and not in
     * form of entities.
     *
     * @return A representation D3.js can handle
     */
    public Map<String, List<Object>> f() {

        var nodes = new ArrayList<>();
        var links = new ArrayList<>();

        try (Session session = driver.session()) {
            var records = session.readTransaction(tx -> tx.run("" + " MATCH (m:Movie) <- [r:ACTED_IN] - (p:Person)"
                    + " WITH m, p ORDER BY m.title, p.name" + " RETURN m.title AS movie, collect(p.name) AS actors")
                    .list());
            records.forEach(record -> {
                var movie = Map.of("label", "movie", "title", record.get("movie").asString());

                var targetIndex = nodes.size();
                nodes.add(movie);

                record.get("actors").asList(v -> v.asString()).forEach(name -> {
                    var actor = Map.of("label", "actor", "title", name);

                    int sourceIndex;
                    if (nodes.contains(actor)) {
                        sourceIndex = nodes.indexOf(actor);
                    } else {
                        nodes.add(actor);
                        sourceIndex = nodes.size() - 1;
                    }
                    links.add(Map.of("source", sourceIndex, "target", targetIndex));
                });
            });
        }
        return Map.of("nodes", nodes, "links", links);
    }

}
