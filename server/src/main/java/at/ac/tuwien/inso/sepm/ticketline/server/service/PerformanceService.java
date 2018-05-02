package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;

import java.util.List;

public interface PerformanceService {

    List<Performance> findAll();

    Performance findOne(Long id);
}
