package rightshot.vo;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVO<E extends Serializable> {

	private List<E> content;
	private long totalElements;

	private String sort;
	private String sortDirection;
	private int pageIndex;
	private int pageSize;
	private boolean changedQuery;
	
	private Object filterForm;

	public PageVO(String sort, String sortDirection, int pageIndex, int pageSize) {
		this.sort = sort;
		this.sortDirection = sortDirection;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.changedQuery = false;
	}

	public PageVO(List<E> content, int totalElements, String sort, String sortDirection, int pageIndex, int pageSize) {
		this.content = content;
		this.totalElements = totalElements;
		this.sort = sort;
		this.sortDirection = sortDirection;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.changedQuery = false;
	}

	public PageVO(List<E> content, long totalElements, String sort, String sortDirection, int pageIndex, int pageSize,
			Object filterForm) {
		this.content = content;
		this.totalElements = totalElements;
		this.sort = sort;
		this.sortDirection = sortDirection;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.filterForm = filterForm;
		this.changedQuery = true;
	}

}
