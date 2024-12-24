function Comparer()
{
	function init()
	{
		addCTA()
	}

	/**
	 * Add compare btn if not in the compare page
	*/
	function addCTA() {
		if (COMPARING) {
			getData()
			return
		}

		const modal_cta_el = $(`
			<div class="compare-cta gs--up">
				<a href="/wines/compare" class="compare-cta--btn">
					Compare wines<i class="material-symbols-rounded">compare_arrows</i>
				</a>
			</div>`)
			
		$('body').append(modal_cta_el)

		bindCTA()
	}

	// Handle compare btn click
	function bindCTA()
	{
		$('.compare-cta--btn').unbind().click(getData)
	}

	function getData()
	{
		$.ajax({
			url: '/api/multi-query',
			type: 'get',
			success(res) {
				console.log(res)
			}
		})
	}
 
	return {
		init
	}
}


Comparer().init()