package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorCategoryDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.sector.SectorCategoryMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.SectorCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/sector_category")
@Api(value = "sector_category")
public class SectorCategoryEndpoint {

    private final SectorCategoryService sectorCategoryService;
    private final SectorCategoryMapper sectorCategoryMapper;

    public SectorCategoryEndpoint(SectorCategoryService sectorCategoryService, SectorCategoryMapper sectorCategoryMapper) {
        this.sectorCategoryService = sectorCategoryService;
        this.sectorCategoryMapper = sectorCategoryMapper;
    }

    @GetMapping()
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Get sector category entries ordered by base price modifier (ascending)")
    public List<SectorCategoryDTO> findAllOrderByBasePriceModAsc() {
        return sectorCategoryMapper.sectorCategoryToSectorCategoryDTO(sectorCategoryService.findAllOrderByBasePriceModAsc());
    }
}
