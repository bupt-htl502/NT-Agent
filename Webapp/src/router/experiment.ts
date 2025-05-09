export default [
    {
        path: '/experiment/0',
        name: 'experiment_0',
        component: () => import('@/views/Experiment/Experiment.vue'),
    },
    {
        path: '/experiment/40002',
        name: 'experiment_40002',
        component: () => import('@/views/Feature/CICFlowMeterFeature.vue'),
        props: route => ({ title: route.query.title || 'Default Title' })
    },
    {
        path: '/experiment/40003',
        name: 'experiment_40003',
        component: () => import('@/views/Feature/dpktFeature.vue'),
        props: route => ({ title: route.query.title || 'Default Title' })
    },
    {
        path: '/experiment/40004',
        name: 'experiment_40004',
        component: () => import('@/views/Feature/normalFeature.vue'),
        props: route => ({ title: route.query.title || 'Default Title' })
    },
    {
        path: '/experiment/40006',
        name: 'experiment_40006',
        component: () => import('@/views/Feature/Byteoffsetfieldextraction.vue'),
        props: route => ({ title: route.query.title || 'Default Title' })
    },
    {
        path: '/experiment/40007',
        name: 'experiment_40007',
        component: () => import('@/views/Feature/Pysharkfieldextraction.vue'),
        props: route => ({ title: route.query.title || 'Default Title' })
    },
    {
        path: '/experiment/40009',
        name: 'experiment_40009',
        component: () => import('@/views/Feature/Trafficbyteextraction.vue'),
        props: route => ({ title: route.query.title || 'Default Title' })
    },
    {
        path: '/experiment/40010',
        name: 'experiment_40010',
        component: () => import('@/views/Feature/Convertbytesintoimages.vue'),
        props: route => ({ title: route.query.title || 'Default Title' })
    },
    {
        path: '/experiment/40011',
        name: 'experiment_40011',
        component: () => import('@/views/Feature/Convertbytesintoembeddingvectors.vue'),
        props: route => ({ title: route.query.title || 'Default Title' })
    },
];