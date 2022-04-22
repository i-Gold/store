<template>
  <div>
    <h3 style="margin-top: 5%">{{ name }}</h3>
    <div class="container">
      <form style="margin-top: 5%">
        <fieldset class="form-group">
          <label>ID</label>
          <input type="text" class="form-control" v-model="id" disabled />
        </fieldset>
        <fieldset class="form-group">
          <label>Manufacturer</label>
          <input
            type="text"
            class="form-control"
            v-model="manufacturer"
            disabled
          />
        </fieldset>
        <fieldset class="form-group">
          <label>Weight</label>
          <input type="number" class="form-control" v-model="weight" disabled />
        </fieldset>
        <fieldset class="form-group">
          <label>Produced Date</label>
          <input
            type="date"
            class="form-control"
            v-model="producedAt"
            disabled
          />
        </fieldset>
        <fieldset class="form-group">
          <label>Expired Date</label>
          <input
            type="date"
            class="form-control"
            v-model="expiredAt"
            disabled
          />
        </fieldset>
      </form>
    </div>
  </div>
</template>

<script>
import ProductService from '../services/product.service';

export default {
  name: 'Product',
  data() {
    return {
      name: '',
      manufacturer: '',
      weight: 0.0,
      producedAt: new Date().toISOString().slice(0, 10),
      expiredAt: new Date().toISOString().slice(0, 10)
    };
  },
  computed: {
    id() {
      return this.$route.params.id;
    }
  },
  methods: {
    refreshProductDetails() {
      ProductService.getProductById(this.id).then(res => {
        this.name = res.data.name;
        this.manufacturer = res.data.manufacturer;
        this.weight = res.data.weight;
        this.producedAt = res.data.producedAt;
        this.expiredAt = res.data.expiredAt;
      });
    }
  },
  created() {
    this.refreshProductDetails();
  }
};
</script>

<style></style>
