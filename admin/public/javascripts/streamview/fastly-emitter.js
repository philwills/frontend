define(function () {

    var group,
        Pool,
        particles,
        hitPercentage,
        shaderMaterial;

    function setTargetParticle(p) {
        //var material = new THREE.ParticleCanvasMaterial({
        //                                program: SPARKS.CanvasShadersUtils.circles,
        //                                blending:THREE.AdditiveBlending
        //                            });

        var hue = (_.random(0, 100) <= hitPercentage) ? 0.5 : 0;
        target = Pool.get();


        shaderMaterial.attributes.customColor.value[target].setHSL(hue, 1, 0.6);

        //return particles.vertices[target];

        //h = (h == 0) ? 0.5 : 0;

        /*material.color.setHSL(hue, 1, 0.6); //0.7

        particle = new THREE.Particle( material );

        particle.scale.x = particle.scale.y = 1.4;

        group.add( particle );*/

        return target;
    };

    function onParticleCreated(p) {
        particles.vertices[target] = p.position;
        //var position = p.position;
        //p.target.position = position;

    };

    function onParticleDead(p) {
        //particle.target.visible = false; // is this a work around?
        //group.remove(particle.target);
        particles.vertices[p.target].set(Number.POSITIVE_INFINITY,Number.POSITIVE_INFINITY, Number.POSITIVE_INFINITY);
        Pool.add(p.target);
    };


    return {
        init: function(opts) {
            shaderMaterial = opts.shaderMaterial;
            Pool = opts.pool,
            particles = opts.particles;
            group = opts.group;

            var sparksEmitter,
                totalRequests  = opts.metrics.Hits + opts.metrics.Misses,
                missPercentage = Math.round(opts.metrics.Misses/totalRequests * 100),
                requestsPerSec = totalRequests/ 60;

            hitPercentage  = Math.round(opts.metrics.Hits/totalRequests * 100);

            sparksEmitter = new SPARKS.Emitter(new SPARKS.SteadyCounter(requestsPerSec));

            //var linezone = new SPARKS.LineZone( new THREE.Vector3(200,50,0), new THREE.Vector3(200,250,0) )
            var linezone = new SPARKS.LineZone(
                                    new THREE.Vector3(opts.pos.sx, opts.pos.sy, 0),
                                    new THREE.Vector3(opts.pos.dx, opts.pos.dy, 0)
                                )

            sparksEmitter.addInitializer(new SPARKS.Position(linezone));
            sparksEmitter.addInitializer(new SPARKS.Lifetime(1,2));
            sparksEmitter.addInitializer(new SPARKS.Target(null, setTargetParticle));
            sparksEmitter.addInitializer(new SPARKS.Velocity(new SPARKS.PointZone(new THREE.Vector3(200,0,0))));

            sparksEmitter.addAction(new SPARKS.Age());
            sparksEmitter.addAction(new SPARKS.Move());
            sparksEmitter.addAction(new SPARKS.RandomDrift(500,500,0));
            //sparksEmitter.addAction(new SPARKS.Accelerate(0,-100,0));

            sparksEmitter.addCallback("created", onParticleCreated);
            sparksEmitter.addCallback("dead", onParticleDead);

            sparksEmitter.start();

            // Add label
            var textColor = new THREE.Color(0xAAAAAA);
            var labelText = new THREE.TextGeometry(opts.title, {size: 15, font: 'optimer', height: 2,});
            var labelMesh = new THREE.Mesh(labelText, new THREE.MeshBasicMaterial({color: textColor}));
            labelMesh.dynamic = true;
            labelMesh.position.x = opts.pos.dx;
            labelMesh.position.y = opts.pos.dy - 20;
            group.add(labelMesh);
        }

    }

});