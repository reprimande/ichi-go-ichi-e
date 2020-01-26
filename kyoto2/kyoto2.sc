(
s.waitForBoot{
    var src = "/path/to/eko-plates/plates/EkoPlateGong db3.wav";
    SynthDef(\kyoto2, {|out, sndbuf, envbuf|
        var grain, chainL, chainR, output, t;
        t = Impulse.kr(LFNoise1.kr(LFNoise2.kr(1.3).range(0.1, 0.5)).range(1, 30));
        grain = GrainBuf.ar(
            2,
            t,
            LFNoise2.kr(LFNoise2.kr(0.5).range(0.01, 3)).range(0.1, 2),
            sndbuf,
            LFNoise2.kr(LFNoise2.kr(0.1).range(0.1, 10)).range(-2,2),
            LFNoise2.kr(LFNoise2.kr(0.01).range(0.03, 0.5)).range(0, 1),
            TChoose.kr(t, [1,2,4]),
            LFNoise2.kr(LFNoise2.kr(10).range(0.5, 4)).range(-1, 1),
            envbuf);
        chainL = FFT(LocalBuf(2048), grain[0]);
        chainR = FFT(LocalBuf(2048), grain[1]);
        chainL = PV_ConformalMap(chainL, LFNoise2.kr(0.1).range(0,1), LFNoise2.kr(0.3).range(0,1));
        chainR = PV_ConformalMap(chainR, LFNoise2.kr(0.1).range(0,1), LFNoise2.kr(0.3).range(0,1));
        chainL = PV_BrickWall(chainL,LFNoise0.kr(LFNoise0.kr(0.3).range(0.1,0.5)).range(-1,1));
        chainR = PV_BrickWall(chainR, LFNoise0.kr(LFNoise0.kr(0.3).range(0.1,0.5)).range(-1,1));
        output = [IFFT(chainL), IFFT(chainR)];

        Out.ar(0, LinXFade2.ar(output,grain,LFNoise2.kr(0.3)));
    }).add;

    b = Buffer.readChannel(s, src, channels: [0]);
    c = Buffer.readChannel(s, src, channels: [1]);
    z = Buffer.sendCollection(s, Env([0,1,0], [0.2, 0.2], 8, -8).discretize, 1);

    x = Synth(\kyoto2, [\sndbuf, b, \envbuf, -1]);
    y = Synth(\kyoto2, [\sndbuf, c, \envbuf, -1]);
}
)
