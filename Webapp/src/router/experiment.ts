export default [
    {
        path: '/experiment/0',
        name: 'experiment_0',
        component: () => import('@/views/Experiment/Experiment.vue'),
    },
    {
        path: '/experiment/10001',
        name: 'experiment_10001',
        component: () => import('@/views/Feature/Feature.vue'),
        props: route => ({ title: route.query.title || 'Default Title' })
    },
];