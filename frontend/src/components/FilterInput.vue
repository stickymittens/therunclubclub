<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  label: { type: String, default: 'Filter' },
  min: { type: Number, default: null },
  max: { type: Number, default: null },
  step: { type: Number, default: 1 },
  decimalPlaces: { type: Number, default: 0 }
})

const emit = defineEmits(['updateFilters'])

const minValue = ref(props.min)
const maxValue = ref(props.max)

const handleBlur = () => {
  const minNum = parseFloat(minValue.value)
  const maxNum = parseFloat(maxValue.value)

  if (!isNaN(minNum) && !isNaN(maxNum)) {
    const minFinal = Math.min(minNum, maxNum)
    const maxFinal = Math.max(minNum, maxNum)
    const round = (num) => parseFloat(num.toFixed(props.decimalPlaces))

    minValue.value = round(minFinal)
    maxValue.value = round(maxFinal)

    emit('updateFilters', { min: minValue.value, max: maxValue.value })
  }
}

watch([() => props.min, () => props.max], ([newMin, newMax]) => {
  minValue.value = newMin
  maxValue.value = newMax
})
</script>

<template>
  <div class="filter-input">
    <label>{{ label }}</label>
    <div class="inputs">
      <input
          type="number"
          :step="step"
          v-model.number="minValue"
          @blur="handleBlur"
          placeholder="Min"
      />
      <span>–</span>
      <input
          type="number"
          :step="step"
          v-model.number="maxValue"
          @blur="handleBlur"
          placeholder="Max"
      />
    </div>
  </div>
</template>



<style scoped>
.filter-input {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.inputs {
  display: flex;
  align-items: center;
  gap: 8px;
}

.inputs input {
  width: 80px;
  padding: 6px;
  border: 1px solid #ccc;
  border-radius: 6px;
  text-align: center;
  font-size: 14px;
}

.inputs input:focus {
  outline: none;
  border-color: #3b82f6;
}
</style>
