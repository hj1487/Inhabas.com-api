package com.inhabas.api.web;

import com.inhabas.api.domain.budget.dto.BudgetHistoryCreateForm;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetHistoryModifyForm;
import com.inhabas.api.domain.budget.usecase.BudgetHistoryService;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "회계내역")
@RequestMapping("/budget/history")
@RequiredArgsConstructor
public class BudgetHistoryController {

    private final BudgetHistoryService budgetHistoryService;

    @Operation(summary = "새로운 회계 내역을 추가한다.")
    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력"),
            @ApiResponse(responseCode = "401", description = "총무가 아니면 접근 불가")
    })
    public ResponseEntity<?> createNewHistory(
            @Authenticated MemberId memberId, @Valid @RequestBody BudgetHistoryCreateForm form) {

        budgetHistoryService.createNewHistory(form, memberId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회계 내역을 수정한다.")
    @PutMapping
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력, 또는 id가 존재하지 않는 경우"),
            @ApiResponse(responseCode = "401", description = "총무가 아니면 접근 불가, 다른 총무가 작성한 것 수정 불가")
    })
    public ResponseEntity<?> modifyHistory(
            @Authenticated MemberId memberId, @Valid @RequestBody BudgetHistoryModifyForm form) {

        budgetHistoryService.modifyHistory(form, memberId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회계 내역을 삭제한다.")
    @DeleteMapping("/{historyId}")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력, 또는 id가 존재하지 않는 경우"),
            @ApiResponse(responseCode = "401", description = "총무가 아니면 접근 불가, 다른 총무가 작성한 것 삭제 불가")
    })
    public ResponseEntity<?> deleteHistory(
            @Authenticated MemberId memberId,
            @PathVariable Integer historyId) {

        budgetHistoryService.deleteHistory(historyId, memberId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회계 내역을 검색한다.")
    @GetMapping("/search")
    @ApiResponse(responseCode = "200", description = "검색 결과를 pagination 적용해서 반환, year 값 안주면 전체기간으로 적용됨.")
    public ResponseEntity<Page<BudgetHistoryDetailDto>> searchBudgetHistory(
            @Nullable @RequestParam Integer year,
            @PageableDefault(size = 15, sort = "dateUsed", direction = Direction.DESC) Pageable pageable) {

        Page<BudgetHistoryDetailDto> historyList = budgetHistoryService.searchHistoryList(year, pageable);

        return ResponseEntity.ok(historyList);
    }

    @Operation(summary = "단일 회계 내역을 조회한다.")
    @GetMapping("/{historyId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력, 또는 id가 존재하지 않는 경우"),
    })
    public ResponseEntity<BudgetHistoryDetailDto> getBudgetHistory(@PathVariable Integer historyId) {

        BudgetHistoryDetailDto history = budgetHistoryService.getHistory(historyId);

        return ResponseEntity.ok(history);
    }

    @Operation(summary = "회계 내역이 작성된 기간동안의 연도 목록을 가져온다.")
    @GetMapping("/years")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<Integer>> getAllYearsOfHistory() {

        return ResponseEntity.ok(budgetHistoryService.getAllYearOfHistory());
    }
}
