package io.cityos.cityosair.di;

import dagger.Component;

@PerFragment
@Component(dependencies = {MainComponent.class})
public interface FragmentComponent {
}
