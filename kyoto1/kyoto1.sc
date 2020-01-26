(
s.options.memSize = 8192 * 256;
s.options.maxNodes = 1024 * 512;
s.options.maxSynthDefs = 1024 * 128;
s.options.threads = 6;

s.waitForBoot({
    ~dirt = SuperDirt(2, s);
    ~dirt.loadSoundFiles("/path/to/samples/*");
    ~dirt.start;

    SynthDef(\sbd, {|out,sustain=1,pan|
        var env1 = EnvGen.ar(Env.perc(0.0001, 0.6), timeScale: 1, doneAction:2);
        var env2 = EnvGen.ar(Env.perc(0.0001, 0.1), timeScale: 1);
        var env3 = EnvGen.ar(Env.perc(0.0001, 0.03), timeScale: 1);
        var sig = PMOsc.ar(50 + (env2 * 400), 5 + (env3 * 300), env2 * 10);
        OffsetOut.ar(out, DirtPan.ar(sig, ~dirt.numChannels, pan, env1));
    }).add;

    SynthDef(\sfm, {|out,sustain=1,pan|
        var env1 = EnvGen.ar(Env.perc(0.0001, ExpRand(0.1,0.6)), timeScale: 1, doneAction:2);
        var env2 = EnvGen.ar(Env.perc(ExpRand(0.00001,0.2), ExpRand(0.01, 0.3)), timeScale: 1);
        var env3 = EnvGen.ar(Env.perc(ExpRand(0.00001, 0.3), ExpRand(0.05, 0.2)), timeScale: 1);
        var sig = PMOsc.ar([0,30,60,90] + ExpRand(0,5000) + (env2 * ExpRand(20, 5000)), env3 * ExpRand(100, 5000), env3 * ExpRand(10, 60));
        OffsetOut.ar(out, DirtPan.ar(sig*0.2, ~dirt.numChannels, pan, env1));
    }).add;

    SynthDef(\sbass, {|out,freq=100,sustain=1,pan|
        var env1 = EnvGen.ar(Env.linen(0.00001,1,ExpRand(0.01,0.2)), timeScale: sustain, doneAction:2);
        var env2 = EnvGen.ar(Env.perc(ExpRand(0.00001,0.2), ExpRand(0.01, 0.3)), timeScale: 1);
        var env3 = EnvGen.ar(Env.perc(ExpRand(0.00001, 0.1), ExpRand(0.05, 0.2)), timeScale: 1);
        var sig = PMOsc.ar(freq, freq * env3 * ExpRand(0.5,1.5), env3 * ExpRand(2, 20));
        sig = Fold.ar(sig, -0.9, 0.9);
        OffsetOut.ar(out, DirtPan.ar(sig*0.5, ~dirt.numChannels, pan, env1));
    }).add;

    SynthDef(\ssn, {|out,sustain=1,pan|
        var env1 = EnvGen.ar(Env.perc(0.0001, ExpRand(0.1, 0.5)), timeScale: 1, doneAction:2);
        var env2 = EnvGen.ar(Env.perc(ExpRand(0.00001,0.2), ExpRand(0.01, 0.3)), timeScale: 1);
        var sig = ClipNoise.ar(1);
        sig = RHPF.ar(sig, 700 + (100 * env2), 0.2);
        sig = RLPF.ar(sig, 10000 + (200 * env2), 0.8);
        OffsetOut.ar(out, DirtPan.ar(sig, ~dirt.numChannels, pan, env1));
    }).add;

    SynthDef(\shh, {|out,sustain=1,pan|
        var env1 = EnvGen.ar(Env.perc(0.0001, ExpRand(0.05, 0.3)), timeScale: 1, doneAction:2);
        var sig = ClipNoise.ar(1);
        sig = RHPF.ar(sig, 15000, 0.2);
        sig = RLPF.ar(sig, 18000, 0.3);
        OffsetOut.ar(out, DirtPan.ar(sig, ~dirt.numChannels, pan, env1));
    }).add;

})
)