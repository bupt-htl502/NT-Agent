/**
 * @description 商品子菜单
 */
import { RouteRecordRaw, RouterView } from 'vue-router'

const routes:RouteRecordRaw = {
  path: 'product',
  component: RouterView,
  meta: {
    title: '商品'
  },
  children: [
    {
      path: 'list',
      name: 'product_list',
      component: () => import('@/views/product/list/index.vue'),
      meta: {
        title: '商品列表'
      }
    },
    {
      path: 'category',
      name: 'product_category',
      component: () => import('@/views/product/category/index.vue'),
      meta: {
        title: '商品分类'
      }
    },
    {
      path: 'attr',
      name: 'product_attr',
      component: () => import('@/views/product/attr/index.vue'),
      meta: {
        title: '商品规格'
      }
    }
  ]
}

export default routes

