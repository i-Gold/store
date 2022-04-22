<template>
	<div>
		<h3 align="center" style="margin-top: 1%">ADD PRODUCTS AND CREATE THE WAYBILL</h3>
		<b-alert
			:show="dismissCountDown"
			dismissible
			variant="warning"
			@dismissed="dismissCountDown=0"
			@dismiss-count-down="countDownChanged"
			>
			Fill all gaps! {{ dismissCountDown }} seconds...
		</b-alert>
		<div style="margin-top: 5%">
			<form
				<div class="row justify-content-center"
				v-for="(product, index) in products"
				:key="index"
				>
				<div>
					<strong style="font-size: 26px">
						{{index + 1}}
					</strong>
				</div>
				<div style="padding: 5px">
					<div class="form-group">
						<label class="sr-only">Name</label>
						<input 
						class="form-control"
						placeholder="Enter name"
						v-model="product.name"
						name="products[][name]"
						required
						>
						<div
							v-if="myErrors.includes('name')"
							class="alert alert-danger"
							role="alert"
						>
							Name is required!
						</div>
					</div>
				</div>
				<div style="padding: 5px">
					<div class="form-group">
						<label class="sr-only">Manufacturer</label>
						<input
						class="form-control"
						placeholder="Enter manufacturer"
						v-model="product.manufacturer"
						name="products[][manufacturer]"
						required
						>
						<div
							v-if="myErrors.includes('manufacturer')"
							class="alert alert-danger"
							role="alert"
						>
							Manufacturer is required!
						</div>
					</div>
				</div>
				<div style="padding: 5px; width: 70px">
					<div class="form-group">
						<label class="sr-only">Weight</label>
						<input 
						class="form-control"
						placeholder="0.0"
						v-model="product.weight"
						name="products[][weight]"
						required
						>
						<div
							v-if="myErrors.includes('weight')"
							class="alert alert-danger"
							role="alert"
						>
							Weight is required!
						</div>
					</div>
				</div>
				<div style="padding: 5px">
					<div class="form-group">
						<label class="sr-only">Produced Date</label>
						<input 
						class="form-control"
						placeholder="01-01-2020"
						v-model="product.producedAt"
						name="products[][producedAt]"
						required
						>
						<div
							v-if="myErrors.includes('producedAt')"
							class="alert alert-danger"
							role="alert"
						>
							Produced date is required!
						</div>
					</div>
				</div>
				<div style="padding: 5px">
					<div class="form-group">
						<label class="sr-only">Expired Date</label>
						<input 
						class="form-control"
						placeholder="01-01-2025"
						v-model="product.expiredAt"
						name="products[][expiredAt]"
						required
						>
						<div
							v-if="myErrors.includes('expiredAt')"
							class="alert alert-danger"
							role="alert"
						>
							Expired date is required!
						</div>
					</div>
				</div>
				<div style="padding: 5px">
					<button
					type="button"
					class="btn btn-light"
					@click.prevent="removeProduct(index)"
					v-show="quantity > 1"
					>
					<span aria-hidden="true">×</span>
					Remove
				</button>
				</div>
			</div>
				<div class="row justify-content-center">
					<div class="col-sm-6"></div>
					<div class="col-sm-2">
						<button
							type="button"
							class="btn btn-secondary"
							@click.prevent="addProduct"
							v-show="quantity < 10"
							>
						Add item
						</button>
					</div>
				</div>
			<hr>
		<div class="row justify-content-center">
				<div class="col-sm-2 text-left">
					<button
						type="submit"
						class="btn btn-success"
						@click="saveProducts()"
						>
					CREATE
					</button>
				</div>
			</div>
		</form>
	</div>
</div>
</template>

<script>
	import ProductService from "../services/product.service";
	import WaybillService from "../services/waybill.service";

	export default {
		name: "ProductImport",
		
		data() {
			return {
				dismissSecs: 5,
        		dismissCountDown: 0,
				myErrors: [],
				products: [{ name: '', manufacturer: '', weight: '', producedAt: '', expiredAt: ''}],
			};
		},
		computed: {
			quantity: function () {
				return this.products.length;
			},
		},
		methods: {
			addProduct: function (event) {
				this.myErrors = [];
				for (let i = 0; i < this.products.length; i++) {
					if(this.products[i].name === '') {
						this.myErrors.push('name');
						break;
					}
					if(this.products[i].manufacturer === '') {
						this.myErrors.push('manufacturer');
						break;
					}
					if(this.products[i].weight === '') {
						this.myErrors.push('weight');
						break;
					}
					if(this.products[i].producedAt === '') {
						this.myErrors.push('producedAt');
						break;
					}
					if(this.products[i].expiredAt === '') {
						this.myErrors.push('expiredAt');
						break;
					}
				}
				if(!(this.myErrors.includes('name') || this.myErrors.includes('manufacturer') || this.myErrors.includes('weight') || this.myErrors.includes('producedAt') || this.myErrors.includes('expiredAt'))) {
					event.preventDefault();
					this.products.push({
						name: '',
						manufacturer: '',
						weight: '',
						producedAt : '',
						expiredAt : '',
					});
				}
			},
			removeProduct: function (index) {
				this.products.splice(index, 1);
			},
			async saveProducts() {
				for(var i = 0; i < this.products.length; i++) {
					if(this.products[i].name !== '' && this.products[i].manufacturer !== ''  && this.products[i].weight !== '' && this.products[i].producedAt !== '' && this.products[i].expiredAt !== '') {
						try {
							const productResponse = await ProductService.addProductsForImport(this.products);
							const labelResponse = await WaybillService.createWaybillForImport();
							this.$router.push('/profile');
						} catch(e) {
							console.log(e);
						}
					} else {
						this.showAlert();
					}
				}
			},
			countDownChanged(dismissCountDown) {
				this.dismissCountDown = dismissCountDown
			},
			showAlert() {
				this.dismissCountDown = this.dismissSecs
			}
		}
	};
</script>

<style>

.unit-price {
	margin-right: 2rem;
 	color: #999;
}

</style>


