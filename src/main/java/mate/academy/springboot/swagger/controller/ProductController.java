package mate.academy.springboot.swagger.controller;

import java.math.BigDecimal;
import java.util.List;
import mate.academy.springboot.swagger.dto.ProductDto;
import mate.academy.springboot.swagger.mapper.Mapper;
import mate.academy.springboot.swagger.model.Product;
import mate.academy.springboot.swagger.service.ProductService;
import mate.academy.springboot.swagger.service.SortParamParser;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final SortParamParser sortParamParser;
    private final Mapper mapper;

    public ProductController(ProductService productService, SortParamParser sortParamParser,
                             Mapper mapper) {
        this.productService = productService;
        this.sortParamParser = sortParamParser;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ProductDto> add(@RequestBody ProductDto productDto) {
        Product add = productService.add(mapper.toModel(productDto));
        return ResponseEntity.ok(mapper.toDto(add));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(productService.get(id)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id,
                                             @RequestBody ProductDto productDto) {
        Product update = mapper.toModel(productDto);
        update.setId(id);
        productService.update(update);
        return ResponseEntity.ok(mapper.toDto(update));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAll(@RequestParam(defaultValue = "0") Integer page,
                                                  @RequestParam(defaultValue = "20") Integer size,
                                                  @RequestParam(defaultValue = "id")
                                                       String sortBy) {
        Sort parse = sortParamParser.parse(sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, parse);
        return ResponseEntity.ok(mapper.toDtoList(productService.getAll(pageRequest)));
    }

    @GetMapping("/byPrice")
    public ResponseEntity<List<ProductDto>> getAllByPrice(@RequestParam BigDecimal from,
                                                           @RequestParam BigDecimal to,
                                                           @RequestParam(defaultValue = "0")
                                                               Integer page,
                                                           @RequestParam(defaultValue = "20")
                                                               Integer size,
                                                           @RequestParam(defaultValue = "id")
                                                               String sortBy) {
        Sort parse = sortParamParser.parse(sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, parse);
        return ResponseEntity.ok(mapper.toDtoList(productService
                .getAllByPrice(from, to, pageRequest)));
    }
}
